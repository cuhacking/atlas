import React from "react";
import ReactDOM from "react-dom";

import { com } from "Atlas-common";
import getMessage = com.cuhacking.atlas.common.getMessage;
import AtlasConfig = com.cuhacking.atlas.common.AtlasConfig;
import { Map } from "./Map";

const App = () => {
  return <Map />;
};

ReactDOM.render(<App />, document.getElementById("root"));
