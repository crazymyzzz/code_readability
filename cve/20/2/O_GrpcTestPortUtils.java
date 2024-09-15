
public final class GrpcTestPortUtils {

    private static boolean isDefaultProtocols(List<String> protocols) {
        return protocols.size() == 2 && protocols.contains("TLSv1.3") && protocols.contains("TLSv1.2");
    }

  
}
