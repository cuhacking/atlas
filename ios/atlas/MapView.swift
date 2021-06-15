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
import os

struct MapView: UIViewRepresentable {

    private let mapView: MGLMapView = MGLMapView(frame: .zero, styleURL: MGLStyle.darkStyleURL)

    private let searchViewModel: SearchViewModel = SearchViewModelKt.provideSearchViewModel()

    func makeUIView(context: UIViewRepresentableContext<MapView>) -> MGLMapView {
        mapView.delegate = context.coordinator
        return mapView
    }

    func updateUIView(_ uiView: MGLMapView, context: UIViewRepresentableContext<MapView>) {
    }
    
    func makeCoordinator() -> MapView.Coordinator {
        Coordinator(self, searchViewModel)
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
        var viewModel: SearchViewModel

        init(_ control: MapView, _ viewModel: SearchViewModel) {
            self.control = control
            self.viewModel = viewModel
        }

        func mapView(_ mapView: MGLMapView, didFinishLoading style: MGLStyle) {

            viewModel.dataSource.subscribe { (source: MmappGeoJsonSource?) in
                guard let source = source else { return }
                
                os_log("Loaded data: %@", source)
                
                if let oldSource = mapView.style?.source(withIdentifier: source.id) {
                    mapView.style?.removeSource(oldSource)
                }
                mapView.style?.addSource(source.internalSource)
                
                if let oldLayer = mapView.style?.layer(withIdentifier: DataSourceKt.exampleLayer.id) {
                    mapView.style?.removeLayer(oldLayer)
                }
                
                let layer = DataSourceKt.exampleLayer.getMglLayer(source: source.internalSource)
                mapView.style?.addLayer(layer)
            } onError: { (e: KotlinThrowable) in
                os_log("Error: %@", e)
            } onComplete: {
                
            }
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
