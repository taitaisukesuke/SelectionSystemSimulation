import java.util.ArrayList;
import java.util.Random;

public class AgentGroup {
    private ArrayList<Agent> agents = new ArrayList<>();
    private final int agentNum;
    private final float beta;
    private final int id;

    public AgentGroup(int agentNum, float beta, int id, int gyoumuNUM, int percentage) {
        this.agentNum = agentNum;
        this.beta = beta;
        this.id = id;
        for (int i = 0; i < agentNum; i++) {
            agents.add(new Agent(i, gyoumuNUM, this, percentage));
        }

        this.connectAgents();
    }


    //全てのエージェントをつなぐ
    public void connectAgents() {
        for (int i = 0; i < agents.size(); i++) {
            for (int j = 0; j < agents.size(); j++) {
                agents.get(i).connect(agents.get(j));
            }
        }
    }

    //他のクラスターとつながるエージェントの選抜
    public ArrayList<Agent> pickUpAgentssWithBeta() {
        int pickUpNum;
        ArrayList<Agent> pickUpAgents = new ArrayList<>();
        pickUpNum = (int) (this.agentNum * this.beta);
        for (int i = 0; i < pickUpNum; i++) {
            while (true) {
                int random = new Random().nextInt(agentNum);

                if (!pickUpAgents.contains(agents.get(random))) {
                    pickUpAgents.add(agents.get(random));
                    break;
                }
            }
        }
        return pickUpAgents;
    }

    //グループ内のエージェントのラーニングをさせる
    public void learningAllAgents() {
        for (int i = 0; i < agentNum; i++) {
            agents.get(i).learning();
        }
    }


    //グループ内のエージェントを評価する
    public void evaluatingAllAgents(Company company) {
        for (Agent agent : agents) {
            company.evaluate(agent);
        }
    }

    public int getId() {
        return id;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }
}
