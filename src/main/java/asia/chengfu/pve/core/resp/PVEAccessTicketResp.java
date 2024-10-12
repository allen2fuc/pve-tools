package asia.chengfu.pve.core.resp;

public class PVEAccessTicketResp {
    private String CSRFPreventionToken;
    private String ticket;
    private String username;

    public String getCSRFPreventionToken() {
        return CSRFPreventionToken;
    }

    public void setCSRFPreventionToken(String CSRFPreventionToken) {
        this.CSRFPreventionToken = CSRFPreventionToken;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "AccessTicketResp{" +
                "CSRFPreventionToken='" + CSRFPreventionToken + '\'' +
                ", ticket='" + ticket + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
