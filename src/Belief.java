import java.util.Random;


public class Belief {

    private int value;

    //分散するように定義
    public Belief(int random) {
        int random2 = new Random().nextInt(100);

        if (random > random2) {
            this.value = Setting.BELIEF_NUMS[0];
        } else {
            this.value = Setting.BELIEF_NUMS[1];
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
