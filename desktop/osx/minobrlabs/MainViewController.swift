import Cocoa
import WebKit

class MainViewController: NSViewController, WebFrameLoadDelegate {
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
        webView.stringByEvaluatingJavaScriptFromString("setTemperature('35');")
    }

    override var representedObject: AnyObject? {
        didSet {
        // Update the view, if already loaded.
        }
    }


}

