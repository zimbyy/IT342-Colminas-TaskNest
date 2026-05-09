import { render, screen, fireEvent } from '@testing-library/react';
import { vi } from 'vitest';
import RegisterPage from '../RegisterPage.jsx';

// Mock API module
vi.mock('../../../../shared/api.js', () => ({
  registerUser: vi.fn()
}));

// Mock useNavigate and Link
vi.mock('react-router-dom', () => ({
  useNavigate: () => vi.fn(),
  Link: ({ children, to, ...props }) => <a href={to} {...props}>{children}</a>
}));

describe('RegisterPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  test('renders registration form correctly', () => {
    render(<RegisterPage />);
    
    expect(screen.getByLabelText(/username/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/first name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/last name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/^password$/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/confirm password/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /register/i })).toBeInTheDocument();
  });

  test('updates form fields on input change', () => {
    render(<RegisterPage />);
    
    const usernameInput = screen.getByLabelText(/username/i);
    const emailInput = screen.getByLabelText(/email/i);
    const firstNameInput = screen.getByLabelText(/first name/i);
    const lastNameInput = screen.getByLabelText(/last name/i);
    const passwordInput = screen.getByLabelText(/^password$/i);
    const confirmPasswordInput = screen.getByLabelText(/confirm password/i);
    
    fireEvent.change(usernameInput, { target: { value: 'newuser' } });
    fireEvent.change(emailInput, { target: { value: 'newuser@test.com' } });
    fireEvent.change(firstNameInput, { target: { value: 'New' } });
    fireEvent.change(lastNameInput, { target: { value: 'User' } });
    fireEvent.change(passwordInput, { target: { value: 'newpass123' } });
    fireEvent.change(confirmPasswordInput, { target: { value: 'newpass123' } });
    
    expect(usernameInput.value).toBe('newuser');
    expect(emailInput.value).toBe('newuser@test.com');
    expect(firstNameInput.value).toBe('New');
    expect(lastNameInput.value).toBe('User');
    expect(passwordInput.value).toBe('newpass123');
    expect(confirmPasswordInput.value).toBe('newpass123');
  });
});
