declare module "Atlas-common" {
  type Nullable<T> = T | null | undefined;
  namespace com.cuhacking.atlas.common {
    export function getMessage(): string;

    interface AtlasConfig {
      readonly MAPBOX_KEY: string;
    }

    export const AtlasConfig: AtlasConfig;
  }
}
