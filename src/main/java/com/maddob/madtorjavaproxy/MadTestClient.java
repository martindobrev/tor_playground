package com.maddob.madtorjavaproxy;

import com.msopentech.thali.toronionproxy.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class MadTestClient {

  public static void main(String[] args) throws IOException, InterruptedException {
    final String storageLocation = "torfiles";
    final String configs = "torconfigs";

    String fileStorageLocation = "torfiles";
    OnionProxyManager onionProxyManager = null;
    try {
      onionProxyManager = new JavaOnionProxyManager(
          new JavaOnionProxyContext(
              Files.createTempDirectory(fileStorageLocation).toFile()));
    } catch (IOException ex) {
      //Logger.getLogger(disabled_gui.class.getName()).log(Level.SEVERE, null, ex);
    }
    int totalSecondsPerTorStartup = 4 * 60;
    int totalTriesPerTorStartup = 5;

    // Start the Tor Onion Proxy
    try {
      onionProxyManager.startWithRepeat(totalSecondsPerTorStartup, totalTriesPerTorStartup);
    } catch (InterruptedException e) {
      log.error("INTERRUPTED EXCEPTION {}", e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      log.error("IOException {}", e.getMessage());
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // Start a hidden service listener
    // int hiddenServicePort = 80;
    // int localPort = 9343;
    // String onionAddress = onionProxyManager.publishHiddenService(hiddenServicePort, localPort);

    //System.out.println("Onion address is: " + onionAddress);
    // It can taken anywhere from 30 seconds to a few minutes for Tor to start properly routing
    // requests to to a hidden service. So you generally want to try to test connect to it a
    // few times. But after the previous call the Tor Onion Proxy will route any requests
    // to the returned onionAddress and hiddenServicePort to 127.0.0.1:localPort. So, for example,
    // you could just pass localPort into the NanoHTTPD constructor and have a HTTP server listening
    // to that port.

    // Connect via the TOR network
    // In this case we are trying to connect to the hidden service but any IP/DNS address and port can be
    // used here.
    //Socket clientSocket =
    //    Utilities.socks4aSocketConnection(onionAddress, hiddenServicePort, "127.0.0.1", localPort);

    // Now the socket is open but note that it can take some time before the Tor network has everything
    // connected and connection requests can fail for spurious reasons (especially when connecting to
    // hidden services) so have lots of retry logic.

    System.out.println("Socket is OPEN - SUCCESS!!!");
  }
}
