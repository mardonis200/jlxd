package au.com.jcloud.lxd;

import org.apache.log4j.Logger;

import java.util.List;

import au.com.jcloud.lxd.model.Container;
import au.com.jcloud.lxd.model.Image;
import au.com.jcloud.lxd.model.Operation;
import au.com.jcloud.lxd.service.LxdService;
import au.com.jcloud.lxd.service.LxdServiceCliImpl;
import au.com.jcloud.lxd.service.LxdServiceImpl;

/**
 * Created by david on 16/07/16.
 */
public class App {

    private static final Logger LOG = Logger.getLogger(App.class);

    public static void main(String[] args) {
        LOG.info("LXC START. args="+args.length);
        try {
            if (args.length==0) {
                System.out.println("Usage: jlxd <c|i|o> [name] [start|stop|create|delete]");
                System.out.println("");
                System.out.println("   c = list containers");
                System.out.println("   i = list images");
                System.out.println("   o = list operations");
                System.out.println("   name = a specific instance of one of the above");
                System.out.println("   start|stop|create|delete = only for containers");
                System.exit(1);
            }
            //LxdService service = new LxdServiceCliImpl(new LxdServiceImpl());
            LxdService service = new LxdServiceImpl();
            for (int i=0; i<args.length; i++) {
                if (args[i].equals("c")) {
                    LOG.info("");
                    if (args.length-1==i || args[i+1].equals("o") || args[i+1].equals("i")) {
                        List<Container> containers = service.getContainers();
                        LOG.info("containers=" + containers.size());
                        for (Container container : containers) {
                            LOG.info("container=" + container);
                        }
                    } else if (args.length>i+1 && !args[i+1].equals("o") && !args[i+1].equals("i")) {
                        String name = args[i+1];
                        Container container = service.getContainer(name);
                        i++;
                        if (args.length>i+1 && !args[i+1].equals("o") && !args[i+1].equals("i")) {
                            String operation = args[i+1];
                            switch (operation) {
                                case "start":
                                    LOG.info("starting container=" + container);
                                    service.startContainer(name);
                                    break;
                                case "stop":
                                    LOG.info("stopping container=" + container);
                                    service.stopContainer(name);
                                    break;
                                case "create":
                                    LOG.info("creating new container=" + name);
                                    service.createContainer(name, "alpine/edge/amd64"); // TODO: Alias not valid
                                    break;
                                case "delete":
                                    LOG.info("deleting container=" + container);
                                    service.deleteContainer(name);
                                    break;
                                default:
                                    LOG.info("Unknown container operation: " + operation);
                                    System.exit(1);
                            }
                        } else {
                            LOG.info("container=" + container);
                        }
                    }
                }
                else if (args[i].equals("i")) {
                    LOG.info("");
                    if (args.length-1==i || args[i+1].equals("o") || args[i+1].equals("c")) {
                        List<Image> images = service.getImages();
                        LOG.info("images=" + images.size());
                        for (Image image : images) {
                            LOG.info("image=" + image);
                        }
                    } else if (args.length>i+1 && !args[i+1].equals("o") && !args[i+1].equals("c")) {
                        String name = args[i+1];
                        Image image = service.getImage(name);
                        LOG.info("image=" + image);
                        i++;
                    }
                }
                else if (args[i].equals("o")) {
                    LOG.info("");
                    if (args.length-1==i || args[i+1].equals("i") || args[i+1].equals("c")) {
                        List<Operation> operations = service.getOperations();
                        LOG.info("operations=" + operations.size());
                        for (Operation operation: operations) {
                            LOG.info("operation=" + operation);
                        }
                    } else if (args.length>i+1 && !args[i+1].equals("i") && !args[i+1].equals("c")) {
                        String name = args[i+1];
                        Operation operation = service.getOperation(name);
                        LOG.info("operation=" + operation);
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
        LOG.info("LXC DONE");
    }

}