
public interface CommunityDatabaseType extends DatabaseType {

     default String announcementForCommunitySupport() {
        return getName() + " is a community contributed database, see "+ COMMUNITY_CONTRIBUTED_DATABASES + " for more details";
    }

}