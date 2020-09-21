declare module "Atlas-common" {
  import { GeoJSONSource } from "mapbox-gl";

  type Nullable<T> = T | null | undefined;
  namespace com.cuhacking.atlas.common {
    export function getMessage(): string;

    interface AtlasConfig {
      readonly MAPBOX_KEY: string;
    }

    export const AtlasConfig: AtlasConfig;

    export const exampleDataSource: GeoJSONSource;
  }
}
