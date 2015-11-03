//
//  ViewController.swift
//  minobrlabs
//
//  Created by Dmitry Kharitonov on 03/11/15.
//  Copyright © 2015 Astrakhan State University. All rights reserved.
//

import Cocoa
import WebKit

class ViewController: NSViewController, WebFrameLoadDelegate {
    @IBOutlet weak var webView: WebView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        initWebView()
    }
    
    func initWebView() {
        let path = NSBundle.mainBundle().pathForResource("index", ofType: "html", inDirectory: "web")!
        let url = NSURL(fileURLWithPath: path)
        let request = NSURLRequest(URL: url)
        
        webView.frameLoadDelegate = self
        webView.mainFrame.loadRequest(request)
    }
    
    func webView(sender: WebView!, didFinishLoadForFrame frame: WebFrame!) {
        webView.stringByEvaluatingJavaScriptFromString("setTemperature('500');")
    }

    override var representedObject: AnyObject? {
        didSet {
        // Update the view, if already loaded.
        }
    }


}

