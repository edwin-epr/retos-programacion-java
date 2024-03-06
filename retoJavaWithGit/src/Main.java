import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        int exitCode;
        StringBuilder codedMessage = new StringBuilder();

        int number = 1;
        while (number <= 112) {
            String branchId = getBranchId(number);
            String branchName = String.format("ramona-%s", branchId);
            ProcessBuilder changeBranch = new ProcessBuilder("git", "checkout", branchName);
            changeBranch.directory(new File("/home/edwin/Documentos/reto04"));

            Process process1 = changeBranch.start();
            exitCode = process1.waitFor();
            getStatusExitProcess(exitCode);

            ProcessBuilder getMessage = new ProcessBuilder("cat", "mensaje.txt");
            getMessage.directory(new File("/home/edwin/Documentos/reto04"));

            Process process2 = getMessage.start();
            String str = getMessageFromFile(process2);
            codedMessage.append(str);
            exitCode = process2.waitFor();
            getStatusExitProcess(exitCode);
            number++;
        }

        getDecodedMessage(codedMessage);
    }

    public static void getDecodedMessage(StringBuilder codedMessage) {

        System.out.println(codedMessage);
        String messageToDecode = codedMessage.toString();
        String cleanMessageToDecode = messageToDecode.replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(cleanMessageToDecode);
        String messageDecoded = new String(decoded);
        System.out.println("El mensaje secreto es: " + messageDecoded);
    }

    public static String getMessageFromFile(Process process) {
        String linea = null;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            linea = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linea;
    }

    public static void getStatusExitProcess(int exitCode) {
        // Obtener el estado de salida del proceso
        if (exitCode != 0) {
            System.out.println("El comando falló con código de salida " + exitCode);
        }
    }

    public static String getBranchId(int number) {
        if (number < 10) {
            return String.format("00%d", number);
        } else if (number < 100) {
            return String.format("0%d", number);
        } else {
            return String.format("%d", number);
        }
    }
}
