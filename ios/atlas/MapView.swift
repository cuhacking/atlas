//
//  MapView.swift
//  atlas
//
//  Created by Will Mahler on 2020-09-07.
//  Copyright © 2020 cuHacking. All rights reserved.
//

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

            let theCafCoordinates = [
                CLLocationCoordinate2D(latitude: 45.386891, longitude: -75.697635), // bottom left
                CLLocationCoordinate2D(latitude: 45.386930, longitude: -75.696871), // bottom right
                CLLocationCoordinate2D(latitude: 45.387305, longitude: -75.696865), // top right
                CLLocationCoordinate2D(latitude: 45.387300, longitude: -75.697653)  // top left
            ]

            let buildingFeature = MGLPolygonFeature(coordinates: theCafCoordinates, count: 4)
            let shapeSource = MGLShapeSource(identifier: "buildingSource", features: [buildingFeature], options: nil)
            mapView.style?.addSource(shapeSource)

            let fillLayer = MGLFillStyleLayer(identifier: "buildingFillLayer", source: shapeSource)
            fillLayer.fillColor = NSExpression(forConstantValue: UIColor.red)
            fillLayer.fillOpacity = NSExpression(forConstantValue: 0.3)

            mapView.style?.addLayer(fillLayer)

        }

        func mapView(_ mapView: MGLMapView, viewFor annotation: MGLAnnotation) -> MGLAnnotationView? {
            return nil
        }

        func mapView(_ mapView: MGLMapView, annotationCanShowCallout annotation: MGLAnnotation) -> Bool {
            return true
        }

    }
}
