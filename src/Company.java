public class Company {
    private final Weight[] weights;
    private final int syuyakudo;
    private final int gyomuNum;

    public Company(int gyoumuNum, int syuyakudo) {

        if (gyoumuNum % syuyakudo != 0) {
            throw new IllegalArgumentException("syuuyakudoはgyoymuNumの約数である必要があります。");
        }
        this.gyomuNum = gyoumuNum;
        this.syuyakudo = syuyakudo;
        weights = new Weight[gyoumuNum];

        for (int i = 0; i < gyoumuNum; i++) {
            this.weights[i] = new Weight();
        }
    }

    //集約度を評価に組み込む
    public int evaluate(Agent agent) {
        int score = 0;

        for (int i = 0; i < gyomuNum / syuyakudo; i++) {
            boolean isEvaluated = true;
            for (int j = 0; j < syuyakudo; j++) {
                int number = i * syuyakudo + j;

                if (!weights[number].evaluate(agent.getBeliefs().get(number))) {
                    isEvaluated = false;
                    break;
                }
            }
            if (isEvaluated) {
                score += 1;
            }
        }
        agent.setScore(score);
        System.out.println(agent.getMyagentGroup().getId() + "-" + agent.getId() + "のスコアは" + score + "です。");
        return score;
    }
}
