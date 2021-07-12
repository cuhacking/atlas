//
//  ContentView.swift
//  atlas
//
//  Created by Derek Ellis on 2020-06-28.
//  Copyright © 2020 cuHacking. All rights reserved.
//

import SwiftUI

struct ContentView: View {

    @State var isSearchResultsVisible = false

    var body: some View {
        ZStack(alignment: .top) {
            MapView().centerCoordinate(.init(latitude: 45.386438, longitude: -75.696127))
                .zoomLevel(14.661)
                .onTapGesture {
                    UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to:nil, from:nil, for:nil)
                    isSearchResultsVisible = false
                }
            
            SearchView(isSearchResultsVisible: $isSearchResultsVisible)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
