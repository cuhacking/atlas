import React from "react";
import ReactDOM from "react-dom";

import { com } from "Atlas-common";
import AtlasConfig = com.cuhacking.atlas.common.AtlasConfig;
import { Map } from "./Map";

const App = () => {
  return <Map />;
};

ReactDOM.render(<App />, document.getElementById("root"));
