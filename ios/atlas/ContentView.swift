//
//  ContentView.swift
//  atlas
//
//  Created by Derek Ellis on 2020-06-28.
//  Copyright © 2020 cuHacking. All rights reserved.
//

import SwiftUI
import Mapbox

struct ContentView: View {
    var body: some View {
        MapView().centerCoordinate(.init(latitude: 45.386438, longitude: -75.696127)).zoomLevel(14.661)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
