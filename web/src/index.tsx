import React from "react";
import ReactDOM from "react-dom";

import { com } from "Atlas-common";
import getMessage = com.cuhacking.atlas.common.getMessage;

const App = () => {
  return <h1>{getMessage()}</h1>;
};

ReactDOM.render(<App />, document.getElementById("root"));
