declare module "Atlas-common" {
  type Nullable<T> = T | null | undefined;
  namespace com.cuhacking.atlas.common {
    export function getMessage(): string;

    interface BuildKonfig {
      readonly MAPBOX_KEY: string;
    }

    export const BuildKonfig: BuildKonfig;
  }
}
