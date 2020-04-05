package com.maddob.madtorjavaproxy;

import com.msopentech.thali.toronionproxy.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;

import static java.nio.file.Files.*;

public class MadTestClient {

  public static void main(String[] args) throws IOException, InterruptedException {
    final String storageLocation = "torfiles";
    final String configs = "torconfigs";

    final File tempRootDirectory = Files.createTempDirectory(storageLocation).toFile();

    FileUtils.copyDirectory(new File("./src/main/resources"), tempRootDirectory);

    TorConfig torConfig = TorConfig.createDefault(tempRootDirectory);

    //TorConfig torConfig = TorConfig.createFlatConfig(Files.createTempDirectory(storageLocation).toFile());

    //torConfig = new TorConfig.Builder(Files.createTempDirectory(storageLocation).toFile(), Files.createTempDirectory(configs).toFile()).build();
    TorInstaller torInstaller = new JavaTorInstaller(torConfig);
    //torInstaller.setup();


    OnionProxyContext context = new JavaOnionProxyContext(torConfig, torInstaller, new DefaultSettings());
    OnionProxyManager onionProxyManager = new OnionProxyManager(context);

    onionProxyManager.setup();

    int totalSecondsPerTorStartup = 4 * 60;
    int totalTriesPerTorStartup = 5;

    System.out.println("Starting the proxy...");
    // Start the Tor Onion Proxy
    if (onionProxyManager.startWithRepeat(totalSecondsPerTorStartup, totalTriesPerTorStartup, true) == false) {
      return;
    }

    // Start a hidden service listener
    int hiddenServicePort = 80;
    int localPort = 9343;
    String onionAddress = onionProxyManager.publishHiddenService(hiddenServicePort, localPort);

    System.out.println("Onion address is: " + onionAddress);
    // It can taken anywhere from 30 seconds to a few minutes for Tor to start properly routing
    // requests to to a hidden service. So you generally want to try to test connect to it a
    // few times. But after the previous call the Tor Onion Proxy will route any requests
    // to the returned onionAddress and hiddenServicePort to 127.0.0.1:localPort. So, for example,
    // you could just pass localPort into the NanoHTTPD constructor and have a HTTP server listening
    // to that port.

    // Connect via the TOR network
    // In this case we are trying to connect to the hidden service but any IP/DNS address and port can be
    // used here.
    Socket clientSocket =
        Utilities.socks4aSocketConnection(onionAddress, hiddenServicePort, "127.0.0.1", localPort);

    // Now the socket is open but note that it can take some time before the Tor network has everything
    // connected and connection requests can fail for spurious reasons (especially when connecting to
    // hidden services) so have lots of retry logic.

    System.out.println("Socket is OPEN - SUCCESS!!!");
  }
}
