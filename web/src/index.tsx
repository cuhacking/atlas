import React from "react";
import ReactDOM from "react-dom";

import { com } from "Atlas-common";
import getMessage = com.cuhacking.atlas.common.getMessage;
import AtlasConfig = com.cuhacking.atlas.common.AtlasConfig;

const App = () => {
  return (
    <>
      <h1>{getMessage()}</h1>
      <h2>{AtlasConfig.MAPBOX_KEY}</h2>
    </>
  );
};

ReactDOM.render(<App />, document.getElementById("root"));
