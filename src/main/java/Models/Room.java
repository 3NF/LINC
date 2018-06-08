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


    public Room(String id, String title, String coverImagePath, String backgroundImage, Member[] members, Assignment[] assignments) {
        super(id, title, coverImagePath);
        this.members = members;
        this.assignments = assignments;
    }
}

