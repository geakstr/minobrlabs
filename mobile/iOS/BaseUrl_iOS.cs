using System;

using Foundation;
using Xamarin.Forms;
using minobrlabs.iOS;

[assembly: Dependency (typeof(BaseUrl_iOS))]
namespace minobrlabs.iOS
{
	public class BaseUrl_iOS : IBaseUrl
	{
		public string Get ()
		{
			return NSBundle.MainBundle.BundlePath;
		}
	}
}

