package learn.field_agent.models;

public class Alias {
    private int aliasID;
    private String name;
    private String persona;
    private int agentId;

    public int getAliasID() {
        return aliasID;
    }

    public void setAliasID(int aliasID) {
        this.aliasID = aliasID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }
}
