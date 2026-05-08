// Content Script for TaskNest Chrome Extension

// Safe messaging utility with error handling
class SafeMessaging {
  static async sendMessage(message, retries = 3) {
    for (let attempt = 1; attempt <= retries; attempt++) {
      try {
        // Check if runtime is available
        if (!chrome.runtime || !chrome.runtime.sendMessage) {
          throw new Error('Chrome runtime not available');
        }

        const response = await new Promise((resolve, reject) => {
          const timeout = setTimeout(() => {
            reject(new Error('Message timeout'));
          }, 5000);

          chrome.runtime.sendMessage(message, (response) => {
            clearTimeout(timeout);
            if (chrome.runtime.lastError) {
              reject(new Error(chrome.runtime.lastError.message));
            } else {
              resolve(response);
            }
          });
        });

        return response;
      } catch (error) {
        console.warn(`Message attempt ${attempt} failed:`, error.message);
        
        if (attempt === retries) {
          console.error('All message attempts failed:', error);
          return { success: false, error: error.message };
        }
        
        // Wait before retrying
        await new Promise(resolve => setTimeout(resolve, 1000 * attempt));
      }
    }
  }
}

// Listen for messages from background script
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  console.log('Content script received message:', message);
  
  // Handle messages that need content script interaction
  switch (message.type) {
    case 'INJECT_TASK_NEST':
      injectTaskNestWidget();
      sendResponse({ success: true });
      break;
      
    default:
      sendResponse({ success: false, error: 'Unknown message type' });
  }
});

// Inject TaskNest widget into the current page
function injectTaskNestWidget() {
  // Check if widget already exists
  if (document.getElementById('tasknest-widget')) {
    return;
  }

  // Create widget container
  const widget = document.createElement('div');
  widget.id = 'tasknest-widget';
  widget.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    width: 300px;
    height: 400px;
    background: white;
    border: 1px solid #ccc;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    z-index: 10000;
    font-family: Arial, sans-serif;
  `;

  // Add widget content
  widget.innerHTML = `
    <div style="padding: 16px; border-bottom: 1px solid #eee;">
      <h3 style="margin: 0; color: #333;">TaskNest</h3>
    </div>
    <div style="padding: 16px;">
      <p>Task management extension</p>
      <button id="tasknest-open-btn" style="
        background: #007bff;
        color: white;
        border: none;
        padding: 8px 16px;
        border-radius: 4px;
        cursor: pointer;
      ">Open TaskNest</button>
    </div>
  `;

  // Add to page
  document.body.appendChild(widget);

  // Add event listener for the button
  document.getElementById('tasknest-open-btn').addEventListener('click', () => {
    chrome.runtime.sendMessage({ type: 'OPEN_POPUP' });
  });
}

// Initialize content script
console.log('TaskNest content script loaded');

// Example: Send message after registration (this would be called from your registration flow)
async function notifyRegistrationComplete(userData) {
  const response = await SafeMessaging.sendMessage({
    type: 'REGISTRATION_COMPLETE',
    data: userData
  });
  
  if (response.success) {
    console.log('Registration notification sent successfully');
  } else {
    console.error('Failed to send registration notification:', response.error);
  }
}

// Export for use in other scripts
if (typeof module !== 'undefined' && module.exports) {
  module.exports = { SafeMessaging, notifyRegistrationComplete };
}
