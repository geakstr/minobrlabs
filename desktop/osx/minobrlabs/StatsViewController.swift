import Cocoa
import WebKit

class StatsViewController: NSViewController, WebFrameLoadDelegate {
    
    @IBOutlet weak var statsWebView: WebView!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        initWebView()
    }
    
    func initWebView() {
        let path = NSBundle.mainBundle().pathForResource("stats", ofType: "html", inDirectory: "web")!
        let url = NSURL(fileURLWithPath: path)
        let request = NSURLRequest(URL: url)
        
        statsWebView.frameLoadDelegate = self
        statsWebView.mainFrame.loadRequest(request)
    }
    
    func webView(sender: WebView!, didFinishLoadForFrame frame: WebFrame!) {
        //webView.stringByEvaluatingJavaScriptFromString("setTemperature('35');")
    }
    
    override var representedObject: AnyObject? {
        didSet {
            // Update the view, if already loaded.
        }
    }
    
    
}

