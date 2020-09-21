import React from "react";
import ReactMapGL, { ViewState, Source, Layer } from "react-map-gl";
import { AutoSizer } from "react-virtualized";
import "mapbox-gl/dist/mapbox-gl.css";

import { com } from "Atlas-common";
import AtlasConfig = com.cuhacking.atlas.common.AtlasConfig;
import exampleDataSource = com.cuhacking.atlas.common.exampleDataSource;

export const Map = () => {
  const [viewport, setViewport] = React.useState<ViewState>({
    longitude: -75.696127,
    latitude: 45.386438,
    zoom: 14.661,
  });

  return (
    <AutoSizer>
      {({ width, height }: { width: number; height: number }) => (
        <ReactMapGL
          width={width}
          height={height}
          viewState={viewport}
          onViewportChange={setViewport}
          mapboxApiAccessToken={AtlasConfig.MAPBOX_KEY}
        >
          <Source {...exampleDataSource}>
            <Layer id="test-fill" type="fill" paint={{}} />
          </Source>
        </ReactMapGL>
      )}
    </AutoSizer>
  );
};
