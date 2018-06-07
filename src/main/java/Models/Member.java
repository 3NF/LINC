package Models;

public class Member
{
    public enum MemberRole
    {
        lecturer,
        seminarLeader,
        student,
        sectionLeader
    }

    private User user;
    private MemberRole role;

    public Member(User user, MemberRole role)
    {
        this.user = user;
        this.role = role;
    }

    public User getUser()
    {
        return user;
    }

    public MemberRole getRole()
    {
        return role;
    }
}
