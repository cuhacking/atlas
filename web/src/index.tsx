import React from "react";
import ReactDOM from "react-dom";

import { com } from "Atlas-common";
import AtlasConfig = com.cuhacking.atlas.common.AtlasConfig;
import { Map } from "./Map";
import { SearchBar } from "./SearchBar";

const App = () => {
  return (
    <>
      <Map />
      <SearchBar />
    </>
  );
};

ReactDOM.render(<App />, document.getElementById("root"));
