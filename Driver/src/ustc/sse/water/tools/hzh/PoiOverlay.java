package ustc.sse.water.tools.hzh;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.cloud.model.CloudItem;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

public class PoiOverlay {
	private AMap mAMap;
	private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
	private List<CloudItem> mPois;

	public PoiOverlay(AMap amap, List<CloudItem> pois) {
		mAMap = amap;
		mPois = pois;
	}

	public void addToMap() {
		for (int i = 0; i < mPois.size(); i++) {
			Marker marker = mAMap.addMarker(getMarkerOptions(i));
			marker.setObject(i);
			mPoiMarks.add(marker);
		}
	}

	protected BitmapDescriptor getBitmapDescriptor(int index) {
		return null;
	}

	private LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder b = LatLngBounds.builder();
		for (int i = 0; i < mPois.size(); i++) {
			b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
					mPois.get(i).getLatLonPoint().getLongitude()));
		}
		return b.build();
	}

	private MarkerOptions getMarkerOptions(int index) {
		return new MarkerOptions()
				.position(
						new LatLng(mPois.get(index).getLatLonPoint()
								.getLatitude(), mPois.get(index)
								.getLatLonPoint().getLongitude()))
				.title(getTitle(index)).snippet(getSnippet(index))
				.icon(getBitmapDescriptor(index));
	}

	public int getPoiIndex(Marker marker) {
		for (int i = 0; i < mPoiMarks.size(); i++) {
			if (mPoiMarks.get(i).equals(marker)) {
				return i;
			}
		}
		return -1;
	}

	public CloudItem getPoiItem(int index) {
		if (index < 0 || index >= mPois.size()) {
			return null;
		}
		return mPois.get(index);
	}

	protected String getSnippet(int index) {
		return mPois.get(index).getSnippet();
	}

	protected String getTitle(int index) {
		return mPois.get(index).getTitle();
	}

	public void removeFromMap() {
		for (Marker mark : mPoiMarks) {
			mark.remove();
		}
	}

	public void zoomToSpan() {
		if (mPois != null && mPois.size() > 0) {
			if (mAMap == null) {
				return;
			}
			LatLngBounds bounds = getLatLngBounds();
			mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
		}
	}
}
