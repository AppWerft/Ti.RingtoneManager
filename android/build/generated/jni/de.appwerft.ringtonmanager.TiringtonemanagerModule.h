/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2011-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

/** This is generated, do not edit by hand. **/

#include <jni.h>

#include "Proxy.h"

		namespace de {
		namespace appwerft {
		namespace ringtonmanager {


class TiringtonemanagerModule : public titanium::Proxy
{
public:
	explicit TiringtonemanagerModule(jobject javaObject);

	static void bindProxy(v8::Handle<v8::Object> exports);
	static v8::Handle<v8::FunctionTemplate> getProxyTemplate();
	static void dispose();

	static v8::Persistent<v8::FunctionTemplate> proxyTemplate;
	static jclass javaClass;

private:
	// Methods -----------------------------------------------------------
	static v8::Handle<v8::Value> getDefaultUri(const v8::Arguments&);
	static v8::Handle<v8::Value> getCurrentRingtone(const v8::Arguments&);
	static v8::Handle<v8::Value> setActualDefaultRingtone(const v8::Arguments&);

	// Dynamic property accessors ----------------------------------------

};

		} // ringtonmanager
		} // appwerft
		} // de
