

//諸々の設定をここでいじれるよ


public class Setting {
    public static final int[] BELIEF_NUMS = {0, 1};  //信条の値
    public static final int[] WEIGHT_NUMS = {0, 1};  //評価の値

    public static final int AGENT_GROUP_NUM = 10;  //クラスターの数
    public static final int AGENT_NUM_IN_ONE_GROUP = 12;  //１クラスター内のエージェントの数
    public static final float BETA = 0.3f;  //結合度
    public static final int GYOUMU_NUM = 300;  //評価次元の数
    public static final int SYUYAKUDO = 5;  //業務の集約度
    public static final int UPDATE_NUM = 100;  //試行回数
    public static final int LIFE_OF_AGENT = 15;  //退職までの時間
    public static final int PERCENTAGE_OF_LEANING = 20;  //学習する確率
}

