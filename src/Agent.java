import java.util.ArrayList;
import java.util.Random;

public class Agent {
    private ArrayList<Agent> connectedAgents = new ArrayList<>();
    private AgentGroup myagentGroup;
    private final int id;
    private ArrayList<Belief> beliefs = new ArrayList<>();
    private int score = 0;
    private int percentage;


    public Agent(int agentId, int gyoumuNum, AgentGroup myagentGroup, int percentage) {
        this.id = agentId;
        this.myagentGroup = myagentGroup;
        this.percentage = percentage;

        int random = new Random().nextInt(100);

        for (int i = 0; i < gyoumuNum; i++) {
            this.beliefs.add(new Belief(random));
        }
    }

    public boolean isConnected(Agent opponet) {
        return this.connectedAgents.contains(opponet) && opponet.getConnectedAgents().contains(this);
    }


    public Agent[] connect(Agent opponent) {
        if (opponent != null && !isConnected(opponent) && !opponent.isConnected(this)) {
            connectedAgents.add(opponent);
            opponent.connect(this);

            return new Agent[]{this, opponent};
        } else {
            return null;
        }
    }


    private Agent findChampion() {
        Agent champion = null;

        for (int i = 0; i < connectedAgents.size(); i++) {
            if (champion == null) {
                champion = connectedAgents.get(i);
            } else if (champion.getScore() < connectedAgents.get(i).getScore()) {
                champion = connectedAgents.get(i);
            }
        }
        return champion;
    }

    public void learning() {
        Agent champion = this.findChampion();
        int sum = 0;

        for (int i = 0; i < beliefs.size(); i++) {
            int p = new Random().nextInt(100);
            if (beliefs.get(i) != champion.beliefs.get(i)) {
                if (percentage >= p) {
                    beliefs.get(i).setValue(champion.beliefs.get(i).getValue());
                    sum++;
                }
            }
        }

        System.out.println(sum + "個アップデート！");
    }

    public void resetConnection() {
        this.connectedAgents.clear();
    }


    public ArrayList<Agent> getConnectedAgents() {
        return connectedAgents;
    }

    public ArrayList<Belief> getBeliefs() {
        return beliefs;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public AgentGroup getMyagentGroup() {
        return myagentGroup;
    }
}
