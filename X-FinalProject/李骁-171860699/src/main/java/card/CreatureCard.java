package card;

import creature.Creature;
import view.MainCanvas;

public class CreatureCard extends Card {

    Creature creature;

    public CreatureCard(Creature creature) {
        super();
        this.creature = creature;

        loadImage("huluwa_card.png");

        System.out.println("card设置鼠标事件");
        // 鼠标点击
        imageView.setOnMousePressed(e -> {
            lastX = imageView.getTranslateX();
            lastY = imageView.getTranslateY();
            lastEventX = e.getSceneX();
            lastEventY = e.getSceneY();
            System.out.println("mouse pressed, v.x: " + lastX + ", v.y: " + lastY + ", e.x: " + lastEventX + ", e.y: "
                    + lastEventY);
        });

        // 鼠标拖动，钱够才允许拖
        imageView.setOnMouseDragged(e -> {
            if (MainCanvas.cardField.getMoney() >= price) {
                //钱够了
                // System.out.println("mouse dragged");
                double dx = lastEventX - e.getSceneX();
                double dy = lastEventY - e.getSceneY();
                double nx = lastX - dx;
                double ny = lastY - dy;
                // System.out.println("nx: " + nx + ", ny: " + ny);
                imageView.setTranslateX(nx);
                imageView.setTranslateY(ny);
            }
        });

        // 鼠标松开
        imageView.setOnMouseReleased(e -> {
            System.out.println("mouse released, sceney: " + e.getSceneY());
            // 能拖动的，且有钱，添加到跑道，或者回到原地
            double dx = e.getSceneX();
            double dy = e.getSceneY();
            boolean releaseOnRunway = false;
            int runwayIndex = 0; // 释放的跑道下标
            for (; runwayIndex < MainCanvas.runways.size(); runwayIndex++) {
                int runwayX = MainCanvas.runways.get(runwayIndex).getPosX();
                int runwayY = MainCanvas.runways.get(runwayIndex).getPosY();
                int runwayWidth = MainCanvas.runways.get(runwayIndex).getWidth();
                if (dy > runwayY && dy < runwayY + runwayWidth) {
                    releaseOnRunway = true;
                    break;
                }
            }
            if (releaseOnRunway == true) {
                System.out.println("释放在跑道" + runwayIndex);
                System.out.println("价格为" + price);
                Creature creatureTmp = new Creature(); // TODO 生成具体的生物
                MainCanvas.runways.get(runwayIndex).addMyCreature(creatureTmp); // TODO Draggable接口
                MainCanvas.cardField.removeCard(this); // 从卡牌区移除这张卡
                MainCanvas.cardField.setMoney(MainCanvas.cardField.getMoney() - price); // 扣掉金币数
            } else {
                // 放回原位
                imageView.setTranslateX(0);
                imageView.setTranslateY(0);
                // imageView.setX(initX);
                // imageView.setY(initY);
            }
        });
    }
}