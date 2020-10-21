package main;

import org.apache.commons.cli.*;

public class Main {
    private static Options argsOptions() {
        Options options = new Options();
        Option number = new Option("n", "numero", true, "Numero de presas");
        number.setRequired(true);
        options.addOption(number);
        return options;
    }

    public static void main(String[] args) {
        Options options = argsOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter helpF = new HelpFormatter();
            System.err.println(e.getMessage());
            helpF.printHelp("OrcsPresa [options]", options);
            System.exit(1);
        }
        int n = 1;
        String val = "";
        try {
            val = cmd.getOptionValue("n");
            n = Integer.parseUnsignedInt(val);
        } catch (NumberFormatException e) {
            System.err.println("Valor " + val + " para n inválido");
        }
        Pantano p = new Pantano(2, n);
        p.run();
    }
}
