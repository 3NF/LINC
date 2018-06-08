package Models;

public class Room extends BasicRoomInfo
{
    private Member[] members;
    private Assignment[] assignments;


    public Member[] getMembers() {
        return members;
    }

    public Assignment[] getAssignments() {
        return assignments;
    }


    public Room(String name, String title, String coverImagePath, String backgroundImage, Member[] members, Assignment[] assignments) {
        super(name, title, coverImagePath);
        this.members = members;
        this.assignments = assignments;
    }
}

