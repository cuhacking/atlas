//
//  MapView.swift
//  atlas
//
//  Created by Will Mahler on 2020-09-07.
//  Copyright © 2020 cuHacking. All rights reserved.
//

import Common
import SwiftUI
import Mapbox

struct MapView: UIViewRepresentable {

    private let mapView: MGLMapView = MGLMapView(frame: .zero, styleURL: MGLStyle.darkStyleURL)

    func makeUIView(context: UIViewRepresentableContext<MapView>) -> MGLMapView {
        mapView.delegate = context.coordinator
        return mapView
    }

    func updateUIView(_ uiView: MGLMapView, context: UIViewRepresentableContext<MapView>) {
    }
    
    func makeCoordinator() -> MapView.Coordinator {
        Coordinator(self)
    }

    func styleURL(_ styleURL: URL) -> MapView {
        mapView.styleURL = styleURL
        return self
    }

    func centerCoordinate(_ centerCoordinate: CLLocationCoordinate2D) -> MapView {
        mapView.centerCoordinate = centerCoordinate
        return self
    }

    func zoomLevel(_ zoomLevel: Double) -> MapView {
        mapView.zoomLevel = zoomLevel
        return self
    }
    
    final class Coordinator: NSObject, MGLMapViewDelegate {
        var control: MapView

        init(_ control: MapView) {
            self.control = control
        }

        func mapView(_ mapView: MGLMapView, didFinishLoading style: MGLStyle) {

            let source = DataSourceKt.exampleDataSource
            mapView.style?.addSource(source.internalSource)

            let fillLayer = MGLFillStyleLayer(identifier: "fill-layer", source: source.internalSource)
            fillLayer.fillColor = NSExpression(forConstantValue: UIColor.darkGray)
            mapView.style?.addLayer(fillLayer)
            
            let outlineLayer = MGLLineStyleLayer(identifier: "outline-layer", source: source.internalSource)
            outlineLayer.lineWidth = NSExpression(forConstantValue: 4)
            outlineLayer.lineColor = NSExpression(forConstantValue: UIColor.purple)
            mapView.style?.addLayer(outlineLayer)

        }

        func mapView(_ mapView: MGLMapView, viewFor annotation: MGLAnnotation) -> MGLAnnotationView? {
            return nil
        }

        func mapView(_ mapView: MGLMapView, annotationCanShowCallout annotation: MGLAnnotation) -> Bool {
            return true
        }

    }
}

struct MapView_Previews: PreviewProvider {
    static var previews: some View {
        /*@START_MENU_TOKEN@*/Text("Hello, World!")/*@END_MENU_TOKEN@*/
    }
}
