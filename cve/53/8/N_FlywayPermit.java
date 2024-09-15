

@Getter
@CustomLog
public class FlywayPermit implements Serializable {


    public FlywayPermit(String owner, Date permitExpiry, Date contractExpiry, boolean trial, boolean redgateEmployee, boolean fromAuth) {
        this.owner = owner;
        this.permitExpiry = permitExpiry;
        this.contractExpiry = contractExpiry;
        this.trial = trial;
        this.redgateEmployee = redgateEmployee;
        this.fromAuth = fromAuth;
    }

    public void print() {
        if (this.tier == null) {
            LOG.info("Flyway OSS Edition " + VersionPrinter.getVersion() + " by Redgate");
        } else {
            LOG.info("Flyway " + this.tier.getDisplayName() + " Edition " + VersionPrinter.getVersion() + " by Redgate");
        }

        if (contractExpiry != null) {
            if ("Online User".equals(this.owner)) {
                if (this.contractExpiry.getTime() == Long.MAX_VALUE) {
                    LOG.debug("License has no expiry date");
                } else {
                    logLicensedUntilIfWithinWindow();
                }
            } else if (!"Anonymous".equals(this.owner)) {
                LOG.info("Licensed to " + this.owner);
                logLicensedUntilIfWithinWindow();
            }
        }

        if (!REFRESH_TOKEN_FILE.exists() && PERMIT_FILE.exists()) {
            if (permitFileOutdated(PERMIT_FILE)) {
                LOG.info("Flyway permit on disk is outdated and cannot be refreshed automatically because there is no refresh token on disk. Please rerun auth");
            } else if (permitExpired()) {
                LOG.info("Flyway permit on disk is expired and cannot be refreshed automatically because there is no refresh token on disk. Please rerun auth");
            }
        }

        if (this.tier == Tier.COMMUNITY && PERMIT_FILE.exists()) {
            LOG.info("No Flyway license detected for this user - using Community Edition. If you expected a Teams/Enterprise license then please add a Flyway license to your account and rerun auth. Alternatively, you can run auth -logout to remove your unlicensed permit on disk");
        }

        if (isTrial()) {
            LOG.warn("You are using a limited Flyway trial license, valid until " + DateUtils.toDateString(this.contractExpiry) + "." +
                             " In " + StringUtils.getDaysString(DateUtils.getRemainingDays(this.contractExpiry)) +
                             " you must either upgrade to a full " + this.tier.getDisplayName() + " license or downgrade to " + Tier.COMMUNITY.getDisplayName() + ".");
        }
        LOG.info("");
    }

}