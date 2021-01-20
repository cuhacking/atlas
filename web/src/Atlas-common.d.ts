declare module "Atlas-common" {
  import { GeoJSONSource } from "mapbox-gl";

  type Nullable<T> = T | null | undefined;
  export namespace com.cuhacking.mapbox {
    class GeoJsonSource {
      readonly id: string;
      readonly type: string;
      data: any;
      setGeoJson(json: string): void;
      setGeoJsonFeature(feature: any): void;
      setGeoJsonGeometry(geometry: any): void;
      setGeoJsonFeatureCollection(featureCollection: any): void;
      toJsObject(): GsoJSONSource;
    }
  }
  export namespace com.cuhacking.atlas.common {
    const AtlasConfig: {
      readonly MAPBOX_KEY: string;
      readonly SERVER_URL: string;
    };
  }
  export namespace com.cuhacking.atlas.common {
    const exampleDataSource: com.cuhacking.mapbox.GeoJsonSource;
  }
}
