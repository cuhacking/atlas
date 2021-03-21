import React from "react";
import ReactDOM from "react-dom";
import styled, { createGlobalStyle } from "styled-components";

import { com } from "Atlas-common";
import AtlasConfig = com.cuhacking.atlas.common.AtlasConfig;
import { Map } from "./Map";
import { SearchBar } from "./SearchBar";
import "fontsource-roboto/400.css";

const GlobalStyle = createGlobalStyle`
  * {
    box-sizing: border-box;
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
  }

  body {
    --primary-font: 'Roboto', sans-serif;
    --color-surface: #FFFFFF;

    font-family: var(--primary-font);
  }
`;

const AppContainer = styled.div`
  width: 100%;
  height: 100%;
`;

const MapOverlays = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  z-index: 99;
`;

const App = () => {
  return (
    <AppContainer>
      <GlobalStyle />
      <MapOverlays>
        <SearchBar />
      </MapOverlays>
      <Map />
    </AppContainer>
  );
};

ReactDOM.render(<App />, document.getElementById("root"));
