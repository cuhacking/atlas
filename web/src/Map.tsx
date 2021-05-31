import React from "react";
import ReactMapGL, { ViewportProps, Source, Layer } from "react-map-gl";
import { AutoSizer } from "react-virtualized";
import "mapbox-gl/dist/mapbox-gl.css";

import { com } from "Atlas-common";
import AtlasConfig = com.cuhacking.atlas.common.AtlasConfig;
import exampleDataSource = com.cuhacking.atlas.common.exampleDataSource;
import exampleLayer = com.cuhacking.atlas.common.exampleLayer;

export const Map = () => {
  const [viewport, setViewport] = React.useState<ViewportProps>({
    longitude: -75.696127,
    latitude: 45.386438,
    zoom: 14.661,
  });

  const { sourceId, ...layer } = exampleLayer.toJsObject();

  return (
    <AutoSizer>
      {({ width, height }: { width: number; height: number }) => (
        <ReactMapGL
          mapStyle="mapbox://styles/mapbox/light-v10"
          width={width}
          height={height}
          {...viewport}
          scrollZoom={{ speed: 1, smooth: true } as unknown as boolean}
          onViewportChange={setViewport}
          mapboxApiAccessToken={AtlasConfig.MAPBOX_KEY}
        >
          <Source {...exampleDataSource.toJsObject()}>
            <Layer {...layer} />
          </Source>
        </ReactMapGL>
      )}
    </AutoSizer>
  );
};
