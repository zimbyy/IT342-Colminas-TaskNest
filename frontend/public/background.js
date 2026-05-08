// Background Service Worker for TaskNest Chrome Extension

// Listen for messages from content scripts or popup
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  console.log('Background received message:', message);
  
  // Handle different message types
  switch (message.type) {
    case 'REGISTRATION_COMPLETE':
      handleRegistrationComplete(message.data, sendResponse);
      return true; // Keep the message channel open for async response
      
    case 'GET_USER_DATA':
      handleGetUserData(sendResponse);
      return true;
      
    default:
      console.warn('Unknown message type:', message.type);
      sendResponse({ success: false, error: 'Unknown message type' });
  }
});

// Handle registration completion
async function handleRegistrationComplete(userData, sendResponse) {
  try {
    // Store user data in chrome.storage
    await chrome.storage.local.set({
      tasknestUser: userData,
      isLoggedIn: true
    });
    
    console.log('User data stored successfully');
    sendResponse({ success: true });
  } catch (error) {
    console.error('Error storing user data:', error);
    sendResponse({ success: false, error: error.message });
  }
}

// Handle getting user data
async function handleGetUserData(sendResponse) {
  try {
    const result = await chrome.storage.local.get(['tasknestUser', 'isLoggedIn']);
    sendResponse({ 
      success: true, 
      data: {
        user: result.tasknestUser,
        isLoggedIn: result.isLoggedIn || false
      }
    });
  } catch (error) {
    console.error('Error getting user data:', error);
    sendResponse({ success: false, error: error.message });
  }
}

// Extension installation
chrome.runtime.onInstalled.addListener(() => {
  console.log('TaskNest extension installed');
});

// Handle extension startup
chrome.runtime.onStartup.addListener(() => {
  console.log('TaskNest extension started');
});
