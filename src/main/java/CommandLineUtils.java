import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CommandLineUtils {
    public static String runCmd(String cmd) {
        Runtime runtime = Runtime.getRuntime();
        StringBuilder b = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(cmd).getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                b.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b.toString();
    }

    public static Map<String, String> parserCommandLine(String[] args) throws ParseException {
        Map<String, String> cmdLineParamsMap = new HashMap<>();

        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("s", "selfSelectStock", true, "self select stock code file path");
        options.addOption("l", "dataRootLoc", true, "dataRootLoc");
        options.addOption("y", "dataYear", true, "dataYear");
        options.addOption("m", "dataMonth", true, "dataMonth");
        options.addOption("d", "dataDay", true, "dataDay");
        CommandLine commandLine = parser.parse(options, args);

        if (commandLine.hasOption('s')) {
            cmdLineParamsMap.put("selfSelectStock", commandLine.getOptionValue('s'));
        }
        if (commandLine.hasOption('l')) {
            cmdLineParamsMap.put("dataRootLoc", commandLine.getOptionValue('l'));
        }
        if (commandLine.hasOption('y')) {
            cmdLineParamsMap.put("dataYear", commandLine.getOptionValue('y'));
        }
        if (commandLine.hasOption('m')) {
            cmdLineParamsMap.put("dataMonth", commandLine.getOptionValue('m'));
        }
        if (commandLine.hasOption('d')) {
            cmdLineParamsMap.put("dataDay", commandLine.getOptionValue('d'));
        }

        return cmdLineParamsMap;
    }
}
