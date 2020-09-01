package com.canadianbacon.smolircc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;

public class Main {
    public static void main(String[] args) {
        String[] acceptableArgs = {"--host", "--port", "--username", "--password", "--tls", "--help"};
        String jarName;
        try {
            jarName = new java.io.File(Main.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            jarName = "Dev.jar";
        }
        if(args.length == 0) {
            System.out.println(
                      " ======================== \n"
                    + "|        SmolIRCC        |\n"
                    + "|Created By CanadianBacon|\n"
                    + "|                        |\n"
                    + "|   Licensed under GPL3  |\n"
                    + " ========================\n"
                    + "\n"
                    + String.format("For usage instructions run '%s --help'", jarName)
            );
            System.exit(0);
        } else {
            HashMap<String, String> arguments = null;
            try {
                arguments = formatArgs(args);
            } catch (InvalidPropertiesFormatException e) {
                System.out.println("Arguments are incorrectly formatted, please check them");
                System.exit(1);
            } catch (InvalidArgumentException e) {
                System.out.println(String.format("%s is not a valid argument", e.getMessage()));
                System.exit(1);
            }

            boolean formatted = true;
            String incorrect = "";
            for(String key : arguments.keySet()) {
                if(Arrays.stream(acceptableArgs).noneMatch(key::equals)) {
                    incorrect = key;
                    formatted = false;
                    break;
                }
            }

            if(!formatted) {
                System.out.println(String.format("%s is not a valid argument",incorrect));
                System.exit(1);
            }

            if(arguments.containsKey("--help")) {
                System.out.println(
                          "SmolIRCC (Smol IRC Client)\n"
                        + "Created By CanadianBacon\n"
                        + String.format("Usage: %s [arguments]\n", jarName)
                        + "\n"
                        + "Available Arguments\n"
                        + "--help : Prints this help information\n"
                        + "--host [host] : Specify the host\n"
                        + "--port [port] : Specify the port to connect to\n"
                        + "--username [username] : Specify a username if required\n"
                        + "--password [password] : Specify a password if required\n"
                        + "--tls [true/false] : Specify if TLS should be used to connect\n"
                );
                System.exit(0);
            } else {
                if(arguments.containsKey("--host") && arguments.containsKey("--port")) {
                    if(arguments.containsKey("--tls") && (arguments.get("--tls").equals("true") || arguments.get("--tls").equals("false"))) {
                        if(arguments.containsKey("--username") || arguments.containsKey("--password")) {
                            if(arguments.containsKey("--username") && arguments.containsKey("--password")) {
                                //Connect with both TLS and Authentication
                                ConnectionManager.getInstance().makeConnection(arguments.get("--host"), Integer.parseInt(arguments.get("--port")), true, arguments.get("--user"), arguments.get("--password"));
                            } else {
                                System.out.println("Both a username and a password must be specified if authenticating");
                                System.exit(1);
                            }
                        } else {
                            //Connect with TLS
                            ConnectionManager.getInstance().makeConnection(arguments.get("--host"), Integer.parseInt(arguments.get("--port")), true);
                        }
                    } else {
                        if(arguments.containsKey("--username") || arguments.containsKey("--password")) {
                            if(arguments.containsKey("--username") && arguments.containsKey("--password")) {
                                //Connect with Authentication
                                ConnectionManager.getInstance().makeConnection(arguments.get("--host"), Integer.parseInt(arguments.get("--port")), false, arguments.get("--user"), arguments.get("--password"));
                            } else {
                                System.out.println("Both a username and a password must be specified if authenticating");
                                System.exit(1);
                            }
                        } else {
                            //Connect without TLS or Authentication
                            ConnectionManager.getInstance().makeConnection(arguments.get("--host"), Integer.parseInt(arguments.get("--port")));
                        }
                    }
                } else {
                    System.out.println("The required arguments --host and --port could not be found");
                    System.exit(1);
                }
            }
        }
    }

    private static HashMap<String, String> formatArgs(String[] args) throws InvalidPropertiesFormatException, InvalidArgumentException {
        HashMap<String, String> out = new HashMap<>();
        String key = "";
        boolean kvswitch = true;
        if(args.length % 2 == 0) {
            for (String s : args) {
                if (kvswitch) {
                    if(!s.startsWith("--")) {
                        throw new InvalidArgumentException(s);
                    }
                    key = s;
                    kvswitch = false;
                } else {
                    out.put(key, s);
                    kvswitch = true;
                }
            }
        } else {
            if(args[0].equals("--help")) {
                out.put("--help", null);
            } else {
                throw new InvalidPropertiesFormatException("Invalid number of arguments (Key without a value)");
            }
        }
        return out;
    }
}
