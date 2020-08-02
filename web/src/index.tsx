import React from "react";
import ReactDOM from "react-dom";

import { com } from "Atlas-common";
import getMessage = com.cuhacking.atlas.common.getMessage;
import BuildKonfig = com.cuhacking.atlas.common.BuildKonfig;

const App = () => {
  return (
    <>
      <h1>{getMessage()}</h1>
      <h2>{BuildKonfig.MAPBOX_KEY}</h2>
    </>
  );
};

ReactDOM.render(<App />, document.getElementById("root"));
