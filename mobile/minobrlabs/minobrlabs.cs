using System;

using Xamarin.Forms;
using System.Net;
using System.Diagnostics;
using System.Threading.Tasks;

namespace minobrlabs
{
	public class App : Application
	{
		public App ()
		{
			var htmlSource = new HtmlWebViewSource ();
			htmlSource.BaseUrl = DependencyService.Get<IBaseUrl> ().Get ();
			htmlSource.Html =
@"<!DOCTYPE html>
<html>
<head>
	<meta charset=utf-8>
	<title>Xamarin Forms</title>
</head>
<body>
	<style>
	* {
		margin: 0;
		padding: 0;
	}

	body {
		background: #fff;
		color: #333;
		font-family: sans-serif;
		font-size: 14px;
		line-height: 22px;
	}
	
	.gauge-wrapper {
		position: relative;
		width: 200px;
	}
	.gauge {
		display: block;
		width: 100%;
		height: 100%;
	}
	.gauge-value {
		position: absolute;
		width: 200px;
		bottom: -14px;
		left: 0;
		text-align: center;	
		color: #333;
		line-height: 14px;	
	}
	</style>

	<div class='gauge-wrapper'>
		<canvas class='gauge' id='temperature'></canvas>
		<div class='gauge-value' id='temperature-value'></div>
    </div>

	<script src='gauge.min.js'></script>
	<script>
	var gaugeOpts = {
	  lines: 12,
	  angle: 0.15,
	  lineWidth: 0.15,
	  pointer: {
	    length: 0.7,
	    strokeWidth: 0.024,
	    color: '#000000'
	  },
	  limitMax: 'false',
	  colorStart: '#6FADCF',
	  colorStop: '#8FC0DA',
	  strokeColor: '#E0E0E0',
	  generateGradient: true
	};

	var temperatureGauge = new Gauge(document.getElementById('temperature')).setOptions(gaugeOpts);
	temperatureGauge.setTextField(document.getElementById('temperature-value'));
	temperatureGauge.maxValue = 3000;
	temperatureGauge.animationSpeed = 100;
	temperatureGauge.set(0);

	function setTemperature(v) {
	  temperatureGauge.set(parseInt(v));
	}
	</script>
</body>
</html>";
	
			var webView = new WebView {
				Source = htmlSource,
				VerticalOptions = LayoutOptions.FillAndExpand
			};

			webView.Navigated += (sender, e) => {
				PassData(webView);
			};
				
			MainPage = new ContentPage {
				Content = new StackLayout {
					Padding = new Thickness (0, 20, 0, 0),
					Children = {
						webView
					}
				}
			};
		}

		protected async void PassData (WebView webView)
		{
			for (int i = 0; i <= 3000; i += 100) { 
				webView.Eval (String.Format("setTemperature('{0}');", i.ToString()));
				await Task.Delay (1000);
			}
		}

		protected override void OnStart ()
		{
			// Handle when your app starts
		}

		protected override void OnSleep ()
		{
			// Handle when your app sleeps
		}

		protected override void OnResume ()
		{
			// Handle when your app resumes
		}
	}
}

