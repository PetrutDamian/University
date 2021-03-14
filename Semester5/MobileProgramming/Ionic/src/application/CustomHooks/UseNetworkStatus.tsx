import { useEffect, useState } from "react";
import { NetworkStatus, Plugins } from "@capacitor/core";

const { Network } = Plugins;

export const useNetworkStatus = () => {
  const [networkState, setNetworkState] = useState(false);
  useEffect(() => {
    const handler = Network.addListener(
      "networkStatusChange",
      handleStatusChange
    );
    Network.getStatus().then(handleStatusChange);
    let canceled = false;
    return () => {
      canceled = true;
      handler.remove();
    };

    function handleStatusChange(status: NetworkStatus) {
      console.log("changed: " + status.connected);
      if (!canceled) setNetworkState(status.connected);
    }
  }, []);
  return networkState;
};
