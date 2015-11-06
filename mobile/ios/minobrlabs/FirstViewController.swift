//
//  FirstViewController.swift
//  minobrlabs
//
//  Created by Dmitry Kharitonov on 06/11/15.
//  Copyright © 2015 Astrakhan State University. All rights reserved.
//

import UIKit

class FirstViewController: UIViewController, UIWebViewDelegate {
    @IBOutlet weak var webView: UIWebView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        initWebView()
    }
    
    func initWebView() {
        let path = NSBundle.mainBundle().pathForResource("index", ofType: "html", inDirectory: "web")!
        let url = NSURL(fileURLWithPath: path)
        let request = NSURLRequest(URL: url)
        
        webView.delegate = self
        webView.loadRequest(request)
    }
    
    func webViewDidFinishLoad(webView : UIWebView) {
        webView.stringByEvaluatingJavaScriptFromString("setTemperature('35');")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}

