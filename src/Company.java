public class Company {
    private final Weight[] weights;
    private final int syuyakydo;
    private final int gyomuNum;

    public Company(int gyoumuNum, int syuyakydo) {

        if (gyoumuNum % syuyakydo != 0) {
            throw new IllegalArgumentException("syuuyakudoはgyoymuNumの約数である必要があります。");
        }
        this.gyomuNum = gyoumuNum;
        this.syuyakydo = syuyakydo;
        weights = new Weight[gyoumuNum];

        for (int i = 0; i < gyoumuNum; i++) {
            this.weights[i] = new Weight();
        }
    }

    public int evaluate(Agent agent) {
        int score = 0;

        for (int i = 0; i < gyomuNum / syuyakydo; i++) {
            boolean isEvaluated = true;
            for (int j = 0; j < syuyakydo; j++) {

                if (!weights[i * syuyakydo + j].evaluate(agent.getBeliefs().get(i * syuyakydo + j))) {
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
