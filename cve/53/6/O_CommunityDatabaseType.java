

public abstract class CommunityDatabaseType extends BaseDatabaseType{

    final public String announcementForCommunitySupport() {
        return getName() + " is a community contributed database, see "+ COMMUNITY_CONTRIBUTED_DATABASES + " for more details";
    }

}