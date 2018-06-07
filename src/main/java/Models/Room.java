package Models;

public class Room extends BasicRoomInfo
{
    private String backgroundImage;
    private Member[] members;
    private Assignment[] assignments;

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public Member[] getMembers() {
        return members;
    }

    public Assignment[] getAssignments() {
        return assignments;
    }


    public Room(String name, String title, String coverImagePath, String backgroundImage, Member[] members, Assignment[] assignments) {
        super(name, title, coverImagePath);
        this.backgroundImage = backgroundImage;
        this.members = members;
        this.assignments = assignments;
    }
}

