

//諸々の設定をここでいじれるよ


public class Setting {
    public static final int[] BELIEF_NUMS = {0, 1};  //信条の値
    public static final int[] WEIGHT_NUMS = {0, 1};  //評価の値

    public static final int AGENT_GROUP_NUM = 10;  //クラスターの数
    public static final int AGENT_NUM_IN_ONE_GROUP = 12;  //１クラスター内のエージェントの数
    public static final float BETA = 0.2f;  //結合度
    public static final int GYOUMU_NUM = 120;  //評価次元の数
    public static final int SYUYAKUDO = 4;  //業務の集約度
    public static final int UPDATE_NUM = 200;  //試行回数
    public static final int CHANGE_CONNECTION_TIME = 10;//クラスターを作り変えるまでの回数
    public static final int PERCENTAGE_OF_LEANING = 20;  //学習する確率

    public static final boolean IS_SELECTION_SYSTEM = true;//選抜制かどうか
    public static final int TIMES_OF_SIMULATING = 10000;//回数


}

