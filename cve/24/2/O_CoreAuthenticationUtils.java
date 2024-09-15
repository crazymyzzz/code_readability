

/**
 * This is {@link CoreAuthenticationUtils}.
 *
 * @author Misagh Moayyed
 * @since 5.2.0
 */

@Slf4j
@UtilityClass
public class CoreAuthenticationUtils {

    

    /**
     * Gets attribute merger.
     *
     * @param mergingPolicy the merging policy
     * @return the attribute merger
     */
    public static IAttributeMerger getAttributeMerger(final String mergingPolicy) {
        switch (mergingPolicy.toLowerCase()) {
            case "multivalued":
            case "multi_valued":
            case "combine":
                return new MultivaluedAttributeMerger();
            case "add":
                return new NoncollidingAttributeAdder();
            case "replace":
            case "overwrite":
            case "override":
                return new ReplacingAttributeAdder();
            default:
                return new BaseAdditiveAttributeMerger() {
                    @Override
                    protected Map<String, List<Object>> mergePersonAttributes(final Map<String, List<Object>> toModify,
                                                                              final Map<String, List<Object>> toConsider) {
                        return new LinkedHashMap<>(toModify);
                    }
                };
        }
    }

    
}
